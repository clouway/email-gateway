package com.clouway.telcong.emailgateway.core

/**
 * @author Georgi Georgiev (georgi.georgiev@clouway.com)
 */
data class TemplatedEmail(
        val id: String?,
        val bodyTemplate: String?,
        val bodyModel: Any?,
        val subjectTemplate: String?,
        val subjectModel: Any?,
        val sender: String?,
        val recipients: Set<String>,
        val type: String?,
        val reference: String?,
        var attachmentFiles: MutableSet<AttachmentFile>?
) {

    private constructor(builder: Builder) : this(
            builder.id,
            builder.bodyTemplate,
            builder.bodyModel,
            builder.subjectTemplate,
            builder.subjectModel,
            builder.sender,
            builder.recipients,
            builder.type,
            builder.reference,
            builder.attachmentFiles
    )

    class Builder {
        @JvmField var id: String? = null
        @JvmField var bodyTemplate: String? = null
        @JvmField var bodyModel: Any? = null;
        @JvmField var subjectTemplate: String? = null;
        @JvmField var subjectModel: Any? = null;
        @JvmField var sender: String? = null;
        @JvmField var recipients: MutableSet<String> = mutableSetOf();
        @JvmField var type: String? = null;
        @JvmField var reference: String? = null;
        @JvmField var attachmentFiles: MutableSet<AttachmentFile> = mutableSetOf()

        fun id(id: String): Builder {
          this.id = id
          return this
        }

        fun body(bodyModel: Any, bodyTemplate: String): Builder {
            this.bodyModel = bodyModel
            this.bodyTemplate = bodyTemplate
            return this
        }

        fun subject(subjectModel: Any, subjectTemplate: String): Builder {
            this.subjectModel = subjectModel
            this.subjectTemplate = subjectTemplate
            return this
        }

        fun subject(subjectTemplate: String): Builder {
            this.subjectTemplate = subjectTemplate
            return this
        }

        fun sender(sender: String): Builder {
            this.sender = sender
            return this
        }

        fun recipient(recipient: String): Builder {
            this.recipients.add(recipient)
            return this
        }

        fun recipients(recipient: Set<String>): Builder {
            this.recipients.addAll(recipient)
            return this
        }

        fun type(type: String): Builder {
            this.type = type;
            return this
        }

        fun reference(reference: String): Builder {
            this.reference = reference;
            return this
        }

        fun attachmentFiles(attachmentFiles: MutableSet<AttachmentFile>): Builder {
            this.attachmentFiles.addAll(attachmentFiles);
            return this
        }

        fun build() = TemplatedEmail(this)
    }
}

/**
 * AttachmentFile is a class that represents an attachment file to an [Email].

 * @author Ivan Lazov <ivan.lazov></ivan.lazov>@clouway.com>
 */
class AttachmentFile(@JvmField val name: String? = null, @JvmField val content: ByteArray? = null)