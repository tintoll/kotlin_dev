package io.tintoll.issueservice.domain

import io.tintoll.issueservice.domain.enums.IssuePriority
import io.tintoll.issueservice.domain.enums.IssueStatus
import io.tintoll.issueservice.domain.enums.IssueType
import jakarta.persistence.*

@Entity
@Table
class Issue (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    var userId: Long,

    @OneToMany(fetch = FetchType.EAGER)
    val comments : MutableList<Comment> = mutableListOf(),


    @Column
    var summary: String,

    @Column
    var description: String,

    @Column
    @Enumerated(EnumType.STRING)
    var type: IssueType,

    @Column
    @Enumerated(EnumType.STRING)
    var priority: IssuePriority,

    @Column
    @Enumerated(EnumType.STRING)
    var status: IssueStatus,

    ) : BaseEntity()